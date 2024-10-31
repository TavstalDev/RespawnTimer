package io.github.tavstal.respawntimer.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class MathUtils {
    /**
     * Clamps the given value to a maximum limit.
     *
     * @param value the value to clamp
     * @param max   the maximum limit
     * @return {@code max} if {@code value} is greater than {@code max}; otherwise, {@code value}
     */
    public static int Clamp(int value, int max) {
        return value > max ? max : value;
    }

    /**
     * Clamps the given value within a specified minimum and maximum range.
     *
     * @param value the value to clamp
     * @param min   the minimum limit
     * @param max   the maximum limit
     * @return {@code min} if {@code value} is less than {@code min};
     *         {@code max} if {@code value} is greater than {@code max};
     *         otherwise, {@code value}
     */
    public static int Clamp(int value, int min, int max) {
        return value < min ? min : (value > max ? max : value);
    }

    /**
     * Calculates the Euclidean distance between two 3D points represented by {@code Vec3}.
     *
     * @param vec   the first 3D point
     * @param other the second 3D point
     * @return the Euclidean distance between {@code vec} and {@code other}
     */
    public static double Distance(Vec3 vec, Vec3 other) {
        double x = other.x - vec.x;
        double y = other.y - vec.y;
        double z = other.z - vec.z;
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Calculates the Euclidean distance between two 3D positions represented by {@code BlockPos}.
     *
     * @param vec   the first position
     * @param other the second position
     * @return the Euclidean distance between {@code vec} and {@code other}, cast to an integer
     */
    public static int Distance(BlockPos vec, BlockPos other) {
        int x = other.getX() - vec.getX();
        int y = other.getY() - vec.getY();
        int z = other.getZ() - vec.getZ();
        return (int) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Checks if the provided string can be parsed as a byte.
     *
     * @param str the string to check
     * @return {@code true} if the string can be parsed as a byte; {@code false} otherwise
     */
    public static boolean isByte(String str) {
        try {
            Byte.parseByte(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the provided string can be parsed as an integer.
     *
     * @param str the string to check
     * @return {@code true} if the string can be parsed as an integer; {@code false} otherwise
     */
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the provided string can be parsed as a float.
     *
     * @param str the string to check
     * @return {@code true} if the string can be parsed as a float; {@code false} otherwise
     */
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the provided string can be parsed as a double (decimal).
     *
     * @param str the string to check
     * @return {@code true} if the string can be parsed as a double; {@code false} otherwise
     */
    public static boolean isDecimal(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
