package com.shimady.ticketbookingapp.controller.dto;

import com.shimady.ticketbookingapp.model.SeatType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response entity for getting seat information")
public record SeatResponse(
        @Schema(example = "1")
        Long id,

        @Schema(example = "A10")
        String number,

        @Schema(example = "500")
        int price,

        @Schema(example = "ECONOMY")
        SeatType type,

        @Schema(example = "true")
        Boolean isBooked
) {
}
