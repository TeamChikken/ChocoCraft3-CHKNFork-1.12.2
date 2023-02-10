package net.chococraft.common.entities.properties;

public enum SaddleType {
	//THE CENTER VALUE IS INV SIZE, + 1 is for saddle slot.
    NONE(false, 0, -1), SADDLE(true, 0, 0), SADDLE_BAGS(true, 15 + 1, 1), PACK(true, 45 + 1, 2);

    private boolean isRidingSaddle;
    private int inventorySize;
    private int meta;

    public boolean isRidingSaddle() {
        return this.isRidingSaddle;
    }

    public int getInventorySize() {
        return this.inventorySize;
    }

    public int getMeta() {
        return this.meta;
    }

    SaddleType(boolean isRidingSaddle, int inventorySize, int meta) {
        this.isRidingSaddle = isRidingSaddle;
        this.inventorySize = inventorySize;
        this.meta = meta;
    }

    @SuppressWarnings("PublicStaticArrayField") // pff, security issues?! yeah, no.
    public final static SaddleType[] ITEM_META = new SaddleType[]{SADDLE, SADDLE_BAGS, PACK};

    public static SaddleType getFromMeta(int meta) {
        if (meta < 0 || meta >= ITEM_META.length) return NONE;
        return ITEM_META[meta];
    }

}
