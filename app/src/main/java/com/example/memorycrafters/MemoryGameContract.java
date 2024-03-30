package com.example.memorycrafters;

import android.provider.BaseColumns;

public class MemoryGameContract {
    private MemoryGameContract() {
    } // Para prevenir instancias accidentales

    public static class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_VALUE = "value";
        public static final String COLUMN_VISIBILITY = "visibility";
        public static final String COLUMN_PARTIDA_ID = "PARTIDA_ID";
    }
}
