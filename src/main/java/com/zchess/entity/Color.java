package com.zchess.entity;

public enum Color {
    WHITE,
        BLACK;

            public Color opposite() {
                    return this == WHITE ? BLACK : WHITE;
                        }
                        }
