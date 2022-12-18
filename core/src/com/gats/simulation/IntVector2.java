package com.gats.simulation;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.io.PrintStream;
import java.io.Serializable;

public class IntVector2 implements Serializable, Vector<IntVector2> {


    public final static IntVector2 X = new IntVector2(1, 0);
    public final static IntVector2 Y = new IntVector2(0, 1);
    public final static IntVector2 Zero = new IntVector2(0, 0);


    /** the x-component of this vector **/
    public int x;

    /** the y-component of this vector **/
    public int y;

    /**
     * Constructs a new Vector at the given Coordinates
     * @param x the x-component
     * @param y the y-component
     */
    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Constructs a vector from the given vector
     * @param v The vector */
    public IntVector2 (IntVector2 v) {
        set(v);
    }

    @Override
    public IntVector2 cpy() {
        return new IntVector2(x, y);
    }

    @Override
    public float len() {
        return (float) Math.sqrt(x*x + y*y);
    }

    @Override
    public float len2() {
        return x*x + y*y;
    }

    @Override
    public IntVector2 limit(float limit) {
        if(this.len() > limit) this.setLength(limit);
        return this;
    }

    @Override
    public IntVector2 limit2(float limit2) {
        if(this.len2() > limit2) this.setLength2(limit2);
        return this;
    }

    @Override
    public IntVector2 setLength(float len) {
        if ((x | y) == 0) return this;
        return scl(len/len());
    }

    @Override
    public IntVector2 setLength2(float len2) {
        if ((x | y) == 0) return this;
        return scl((float) Math.sqrt(len2/len2()));
    }

    @Override
    public IntVector2 clamp(float min, float max) {
        final float len = len();
        if (len == 0f) return this;
        if (len > max) return scl(max/len);
        if (len < min) return scl(min/len);
        return this;
    }

    @Override
    public IntVector2 set(IntVector2 v) {
        x = v.x;
        y = v.y;
        return this;
    }


    public IntVector2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public IntVector2 sub(IntVector2 v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    @Override
    public IntVector2 nor() {
        return setLength(1);
    }

    @Override
    public IntVector2 add(IntVector2 v) {
        x += v.x;
        y += v.y;
        return this;
    }

    @Override
    public float dot(IntVector2 v) {
        return x*v.x + y*v.y;
    }

    @Override
    public IntVector2 scl(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    @Override
    public IntVector2 scl(IntVector2 v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    @Override
    public float dst(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return (float) Math.sqrt(dx * dx + y_d * y_d);
    }

    @Override
    public float dst2(IntVector2 v) {
        final float dx = v.x - x;
        final float y_d = v.y - y;
        return dx * dx + y_d * y_d;
    }

    @Override
    public IntVector2 lerp (IntVector2 target, float alpha) {
        final float invAlpha = 1.0f - alpha;
        this.x = Math.round((x * invAlpha) + (target.x * alpha));
        this.y = Math.round((y * invAlpha) + (target.y * alpha));
        return this;
    }

    @Override
    public IntVector2 interpolate (IntVector2 target, float alpha, Interpolation interpolation) {
        return lerp(target, interpolation.apply(alpha));
    }

    @Override
    public IntVector2 setToRandomDirection () {
        float theta = MathUtils.random(0f, MathUtils.PI2);
        return this.set(Math.round(MathUtils.cos(theta)), Math.round(MathUtils.sin(theta)));
    }

    @Override
    public boolean isUnit () {
        return isUnit(0.1f);
    }

    @Override
    public boolean isUnit (final float margin) {
        return Math.abs(len2() - 1f) < margin;
    }

    @Override
    public boolean isZero() {
        return (x | y) == 0;
    }

    @Override
    public boolean isZero(float margin) {
        return len2() < margin;
    }

    @Override
    public boolean isOnLine (IntVector2 other) {
        return MathUtils.isZero(x * other.y - y * other.x);
    }

    @Override
    public boolean isOnLine (IntVector2 other, float epsilon) {
        return MathUtils.isZero(x * other.y - y * other.x, epsilon);
    }


    @Override
    public boolean isCollinear (IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) > 0f;
    }

    @Override
    public boolean isCollinear (IntVector2 other) {
        return isOnLine(other) && dot(other) > 0f;
    }

    @Override
    public boolean isCollinearOpposite (IntVector2 other, float epsilon) {
        return isOnLine(other, epsilon) && dot(other) < 0f;
    }

    @Override
    public boolean isCollinearOpposite (IntVector2 other) {
        return isOnLine(other) && dot(other) < 0f;
    }

    @Override
    public boolean isPerpendicular (IntVector2 vector) {
        return MathUtils.isZero(dot(vector));
    }

    @Override
    public boolean isPerpendicular (IntVector2 vector, float epsilon) {
        return MathUtils.isZero(dot(vector), epsilon);
    }

    @Override
    public boolean hasSameDirection (IntVector2 vector) {
        return dot(vector) > 0;
    }

    @Override
    public boolean hasOppositeDirection (IntVector2 vector) {
        return dot(vector) < 0;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IntVector2 other = (IntVector2) obj;
        if (x != other.x) return false;
        return y == other.y;
    }

    @Override
    public boolean epsilonEquals (IntVector2 other, float epsilon) {
        if (other == null) return false;
        if (Math.abs(other.x - x) > epsilon) return false;
        return !(Math.abs(other.y - y) > epsilon);
    }

    /** Compares this vector with the other vector, using the supplied epsilon for fuzzy equality testing.
     * @return whether the vectors are the same. */
    public boolean epsilonEquals (float x, float y, float epsilon) {
        if (Math.abs(x - this.x) > epsilon) return false;
        return !(Math.abs(y - this.y) > epsilon);
    }

    /** Compares this vector with the other vector using MathUtils.FLOAT_ROUNDING_ERROR for fuzzy equality testing
     * @param other other vector to compare
     * @return true if vector are equal, otherwise false */
    public boolean epsilonEquals (final IntVector2 other) {
        return epsilonEquals(other, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    /** Compares this vector with the other vector using MathUtils.FLOAT_ROUNDING_ERROR for fuzzy equality testing
     * @param x x component of the other vector to compare
     * @param y y component of the other vector to compare
     * @return true if vector are equal, otherwise false */
    public boolean epsilonEquals (float x, float y) {
        return epsilonEquals(x, y, MathUtils.FLOAT_ROUNDING_ERROR);
    }

    @Override
    public IntVector2 mulAdd(IntVector2 v, float scalar) {
        this.x += v.x * scalar;
        this.y += v.y * scalar;
        return this;
    }

    @Override
    public IntVector2 mulAdd(IntVector2 v, IntVector2 mulVec) {
        this.x += v.x * mulVec.x;
        this.y += v.y * mulVec.y;
        return this;
    }

    @Override
    public IntVector2 setZero() {
        return set(0, 0);
    }

    public Vector2 toFloat() {
        return new Vector2(x, y);
    }

    public IntVector2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
