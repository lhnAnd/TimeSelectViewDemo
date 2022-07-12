package com.example.downloaddemo.model;

public enum EnumTest {
    one(1),two(2),three(3),four(4);

    EnumTest(int i) {
    }

    public int getValue(EnumTest t){
        switch (t){
            case one:
                return 1;
            case two:
                return 2;
            case three:
                return 3;
            case four:
                return 4;
        }
        return -1;
    }
}
