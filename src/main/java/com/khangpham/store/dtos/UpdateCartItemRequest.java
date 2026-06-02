package com.khangpham.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull
    @Min(value = 1, message = "Quantity must larger than 0")
    @Max(value = 100, message = "Quantity maximun is 100")
    private Integer quantity;
}
