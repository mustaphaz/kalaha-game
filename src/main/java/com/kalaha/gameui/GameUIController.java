package com.kalaha.gameui;

import com.kalaha.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameUIController {

    private GameUIService gameUIService;
    private Game game;

    @Autowired
    public GameUIController(GameUIService gameUIService) {
        this.gameUIService = gameUIService;
    }

    @GetMapping("/")
    String getStartPage() {
        return "index";
    }

    @GetMapping(value = "/play")
    public String startGame(Model model) {
        initGame();
        addAttributesToModel(model);
        return "board";
    }

    @PostMapping(value = "/play")
    public String performMove(@ModelAttribute Payload payload, Model model) {
        int chosenIndex = payload.getIndex();
        boolean isSouthTurn = payload.getIsSouthTurn();
        int pitListIndex = isSouthTurn ? chosenIndex : chosenIndex + game.getOffsetNorthPlayer();

        if (game.isPitEmpty(pitListIndex))
            addErrorMessageToModel(model, chosenIndex);
        else
            play(pitListIndex, isSouthTurn);

        checkIfGameIsOver(model);
        addAttributesToModel(model);
        return "board";
    }

    private void play(final int pitListIndex, final boolean isSouthTurn) {
        game.setSouthTurn(isSouthTurn);
        game.play(pitListIndex);
    }

    private void checkIfGameIsOver(Model model) {
        if (game.isGameOver())
            model.addAttribute("gameoverMessage", game.getWinnerMessage());
    }

    private void addAttributesToModel(Model model) {
        BoardHtmlData boardHtmlData = gameUIService.getBoardHtmlDataFrom(game);
        model.addAttribute("boardHtmlData", boardHtmlData);
        model.addAttribute("southTurn", game.isSouthTurn());
    }

    private void addErrorMessageToModel(Model model, final int index) {
        model.addAttribute("errorMessage", String.format("The chosen pit %s contains no stones " +
                "please select another pit", index + 1));
    }

    private void initGame() {
        game = Game.initGame();
    }
}
