package org.team100.lib.encoder;

import java.util.OptionalDouble;

import org.team100.lib.logging.Level;
import org.team100.lib.logging.LoggerFactory;
import org.team100.lib.logging.LoggerFactory.DoubleLogger;
import org.team100.lib.logging.LoggerFactory.OptionalDoubleLogger;
import org.team100.lib.motion.mechanism.RotaryMechanism;
import org.team100.lib.util.Takt;

import edu.wpi.first.math.MathUtil;

public class SimulatedRotaryPositionSensor implements RotaryPositionSensor {
    private final RotaryMechanism m_mechanism;
    // LOGGERS
    private final DoubleLogger m_log_position;
    private final OptionalDoubleLogger m_log_rate;

    private double m_positionRad = 0;
    private double m_timeS = Takt.get();

    public SimulatedRotaryPositionSensor(
            LoggerFactory parent,
            RotaryMechanism motor) {
        LoggerFactory child = parent.child(this);
        m_mechanism = motor;
        m_log_position = child.doubleLogger(Level.TRACE, "position");
        m_log_rate = child.optionalDoubleLogger(Level.TRACE, "rate");
    }

    @Override
    public OptionalDouble getPositionRad() {
        double nowS = Takt.get();
        double dtS = nowS - m_timeS;
        // motor velocity is rad/s
        OptionalDouble velocityRad_S = m_mechanism.getVelocityRad_S();
        if (velocityRad_S.isEmpty())
            return OptionalDouble.empty();
        System.out.println("velocity " + velocityRad_S);
        m_positionRad += velocityRad_S.getAsDouble() * dtS;
        m_positionRad = MathUtil.angleModulus(m_positionRad);
        m_timeS = nowS;
        m_log_position.log(() -> m_positionRad);
        return OptionalDouble.of(m_positionRad);
    }

    @Override
    public OptionalDouble getRateRad_S() {
        // motor velocity is rad/s
        OptionalDouble m_rate = m_mechanism.getVelocityRad_S();
        if (m_rate.isEmpty())
            return OptionalDouble.empty();
        m_log_rate.log(() -> m_rate);
        return m_rate;
    }

    @Override
    public void close() {
        //
    }

}
