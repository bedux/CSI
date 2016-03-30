package interfaces;


public interface DataAttributes {



    enum DataName {
        NoMethod("noMethod"), NoPrivateMethod("noPrivateMethod"), NoPublicMethod("noPublicMethod"), NoProtectedMethod("noProtectedMethod"),
        NOF("noF"), //number of fields
        NoForSTM("noForSTM"), //number of for loop
        NoForeachSTM("noForeachSTM"), //number of for loop
        NoWhile("noWhile"), //number of foreach loop
        NoIf("noIf"),
        NoWord("noWord"), //number of foreach loop
        size("size"),
        NoLine("noLine");
        private String value;

        DataName(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

}

