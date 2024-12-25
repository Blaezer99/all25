package org.team100.lib.sway.system.examples;

import org.team100.lib.sway.math.RandomVector;
import org.team100.lib.sway.math.MeasurementUncertainty;
import org.team100.lib.sway.math.Variance;
import org.team100.lib.sway.math.WhiteNoiseVector;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;

public abstract class Cartesian1D extends NoisyLimitedPlant1D {
    public Cartesian1D(WhiteNoiseVector<N2> w, MeasurementUncertainty<N2> v) {
        super(w, v);
    }

    @Override
    public RandomVector<N2> make(Matrix<N2, N1> x, Variance<N2> Kxx) {
        return new RandomVector<>(x,Kxx);
    }
}
