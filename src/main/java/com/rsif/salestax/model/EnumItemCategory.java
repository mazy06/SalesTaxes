package com.rsif.salestax.model;

public enum EnumItemCategory {
    BOOK_PRODUCT(false),
    FOOD_PRODUCT(false),
    MEDICINE_PRODUCT(false),
    BEAUTY_PRODUCT(false),
    MULTIMEDIA_PRODUCT(false);

    private boolean isImport;

    EnumItemCategory(boolean isImport) {
        this.isImport = isImport;
    }

}
