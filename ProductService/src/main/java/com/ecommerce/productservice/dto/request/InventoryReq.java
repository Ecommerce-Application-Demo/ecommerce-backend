package com.ecommerce.productservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record InventoryReq(@NotNull String skuId,@NotNull Integer warehouseId, @NotNull int quantity) {
}
