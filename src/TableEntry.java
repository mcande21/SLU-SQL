import java.util.Objects;

public abstract class TableEntry implements Comparable<TableEntry> {


    public enum Type {
        Bool,
        Double,
        Int,
        String
    }

    public static TableEntry stringtoTE(String next, Type type) {
        return switch (type) {
            case Int -> new IntTableEntry(Integer.valueOf(next));
            case Bool -> new BoolTableEntry(Boolean.parseBoolean(next));
            case String -> new StringTableEntry(next);
            case Double -> new DoubleTableEntry(Double.parseDouble(next));
        };
    }

    public abstract Type getType();

    public abstract String getData();

    public static class BoolTableEntry extends TableEntry {

        private boolean data;

        public BoolTableEntry(boolean b) {
            data = b;
        }

        @Override
        public Type getType() {
            return Type.Bool;
        }

        @Override
        public String getData() {
            return String.valueOf(data);

        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure {@link Integer#signum
         * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * x.compareTo(y)} must throw an exception if and only if {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code
         * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
         * == signum(y.compareTo(z))}, for all {@code z}.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         * @apiNote It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         */
        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.Bool) {
                // trying to cast to something that doesnt work
                throw new ClassCastException();
            }
            BoolTableEntry ob = (BoolTableEntry) o;

            if (data) {
                // data is true
                if (ob.data) {
                    // other boolean also true
                    return 0;
                } else {
                    // other boolean is false
                    return 1;
                }
            }
            if (!ob.data)
                return 0;
            return -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BoolTableEntry that = (BoolTableEntry) o;
            return data == that.data;
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    public static class DoubleTableEntry extends TableEntry {

        private double data;

        public DoubleTableEntry(double b) {
            data = b;
        }

        @Override
        public Type getType() {
            return Type.Double;
        }

        @Override
        public String getData() {
            return String.valueOf(data);
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure {@link Integer#signum
         * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * x.compareTo(y)} must throw an exception if and only if {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code
         * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
         * == signum(y.compareTo(z))}, for all {@code z}.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         * @apiNote It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         */
        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.Double) {
                // trying to cast to something that doesnt work
                throw new ClassCastException();
            }
            DoubleTableEntry ob = (DoubleTableEntry) o;

            if (data < ob.data) {
                return -1;
            } else if (data == ob.data) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DoubleTableEntry that = (DoubleTableEntry) o;
            return Double.compare(that.data, data) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    public static class IntTableEntry extends TableEntry {

        private Integer data;

        public IntTableEntry(Integer b) {
            data = b;
        }

        @Override
        public Type getType() {
            return Type.Int;
        }

        @Override
        public String getData() {
            return String.valueOf(data);
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure {@link Integer#signum
         * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * x.compareTo(y)} must throw an exception if and only if {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code
         * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
         * == signum(y.compareTo(z))}, for all {@code z}.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         * @apiNote It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         */
        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.Int) {
                // trying to cast to something that doesnt work
                throw new ClassCastException();
            }
            IntTableEntry ob = (IntTableEntry) o;

            if (data < ob.data) {
                return -1;
            } else if (data.equals(ob.data)) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntTableEntry that = (IntTableEntry) o;
            return Objects.equals(data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

    public static class StringTableEntry extends TableEntry {

        private String data;

        public StringTableEntry(String b) {
            data = b;
        }

        @Override
        public Type getType() {
            return Type.String;
        }

        @Override
        public String getData() {
            return data;
        }

        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure {@link Integer#signum
         * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
         * all {@code x} and {@code y}.  (This implies that {@code
         * x.compareTo(y)} must throw an exception if and only if {@code
         * y.compareTo(x)} throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
         * {@code x.compareTo(z) > 0}.
         *
         * <p>Finally, the implementor must ensure that {@code
         * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
         * == signum(y.compareTo(z))}, for all {@code z}.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         * @apiNote It is strongly recommended, but <i>not</i> strictly required that
         * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
         * class that implements the {@code Comparable} interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         */
        @Override
        public int compareTo(TableEntry o) {
            if (o.getType() != Type.String) {
                // trying to cast to something that doesnt work
                throw new ClassCastException();
            }
            StringTableEntry ob = (StringTableEntry) o;

            return data.compareTo(ob.data);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StringTableEntry that = (StringTableEntry) o;
            return Objects.equals(data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }
    }

}

