package com.example.fragment.word;

public class wordcintent {





    private static final int COUNT =4 ;

    static {

    }


    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }



    public static class DummyItem {
        public final String id;
        public final String chinese;
        public final String example;

        public DummyItem(String id, String chinese, String example) {
            this.id = id;
            this.chinese = chinese;
            this.example = example;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
