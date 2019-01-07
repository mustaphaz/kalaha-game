package com.kalaha.gameui;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
class BoardHtmlData {
    List<Integer> rowSouth;
    List<Integer> rowNorth;
    Integer kalahaSouth;
    Integer kalahaNorth;
}
