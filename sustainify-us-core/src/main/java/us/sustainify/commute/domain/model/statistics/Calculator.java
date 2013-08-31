package us.sustainify.commute.domain.model.statistics;

import be.appify.framework.quantity.Length;
import be.appify.framework.quantity.Mass;

public interface Calculator<T> {
    static final Mass MASS_ZERO = Mass.kilograms(0);
    static final Length LENGTH_ZERO = Length.kilometers(0);

    public static final Calculator<Length> LENGTH = new Calculator<Length>() {
        @Override
        public Length add(Length value1, Length value2) {
            return value1.add(value2);
        }

        @Override
        public Length zero() {
            return LENGTH_ZERO;
        }
    };

    public static final Calculator<Mass> MASS = new Calculator<Mass>() {

        @Override
        public Mass add(Mass value1, Mass value2) {
            return value1.add(value2);
        }

        @Override
        public Mass zero() {
            return MASS_ZERO;
        }
    };

    T add(T value1, T value2);

    T zero();
}
