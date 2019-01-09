package com.kalaha.gameui;

import com.kalaha.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public final class GameUIController {

    private final GameUIService gameUIService;
    private final Game game;

    @Autowired
    public GameUIController(final GameUIService gameUIService, final Game game) {
        this.gameUIService = gameUIService;
        this.game = game;
    }

    @GetMapping("/")
    String getStartPage() {
        return "index";
    }

    @GetMapping("/play")
    public String getGame(final Model model) {
        addAttributesToModel(model);
        return "board";
    }

    @PostMapping("/play")
    public String performMove(@ModelAttribute final Payload payload, final Model model) {
        int chosenIndex = payload.getIndex();
        boolean isSouthTurn = payload.getIsSouthTurn();
        int pitListIndex = isSouthTurn ? chosenIndex : chosenIndex + game.getOffsetPlayerNorth();

        if (game.isPitEmpty(pitListIndex))
            addErrorMessageToModel(model, chosenIndex);
        else
            play(pitListIndex, isSouthTurn);

        addGameOverMessageIfGameIsOver(model);
        addAttributesToModel(model);
        return "board";
    }

    private void play(final int pitListIndex, final boolean southTurn) {
        game.setSouthTurn(southTurn);
        game.play(pitListIndex);
    }

    private void addErrorMessageToModel(final Model model, final int index) {
        model.addAttribute("errorMessage", String.format("The chosen pit %s contains no stones "
                + "please select another pit", index + 1));
    }

    private void addGameOverMessageIfGameIsOver(final Model model) {
        if (game.isGameOver())
            model.addAttribute("gameoverMessage", game.getWinnerMessage());
    }

    private void addAttributesToModel(final Model model) {
        BoardHtmlData boardHtmlData = gameUIService.getBoardHtmlDataFrom(game);
        model.addAttribute("boardHtmlData", boardHtmlData);
        model.addAttribute("southTurn", game.isSouthTurn());
    }
}
