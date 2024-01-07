package ru.ystu.encryptionapp.enumeration;

public enum Alphabet {
    RUSSIAN {
        @Override
        public char[][] getCharacters() {
            return new char[][] {
                    {'А', 'Б', 'В', 'Г', 'Д', 'Е'},
                    {'Ё', 'Ж', 'З', 'И', 'Й', 'К'},
                    {'Л', 'М', 'Н', 'О', 'П', 'Р'},
                    {'С', 'Т', 'У', 'Ф', 'Х', 'Ц'},
                    {'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь'},
                    {'Э', 'Ю', 'Я', ',', '.', '-'}
            };
        }

        @Override
        public int getRowsNumber() {
            return 6;
        }

        @Override
        public int getColumnsNumber() {
            return 6;
        }
    },
    ENGLISH {
        @Override
        public char[][] getCharacters() {
            return new char[][] {
                    {'A', 'B', 'C', 'D', 'E'},
                    {'F', 'G', 'H', 'I', 'K'},
                    {'L', 'M', 'N', 'O', 'P'},
                    {'Q', 'R', 'S', 'T', 'U'},
                    {'V', 'W', 'X', 'Y', 'Z'}
            };
        }

        @Override
        public int getRowsNumber() {
            return 5;
        }

        @Override
        public int getColumnsNumber() {
            return 5;
        }
    };

    public abstract char[][] getCharacters();

    public abstract int getRowsNumber();
    public abstract int getColumnsNumber();
}