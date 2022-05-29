package com.ofek2608.gim_old;

import com.ofek2608.utils.MathUtils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

public interface GimValue {
	boolean hasValue();

	@Contract("!null->!null")
	@Nullable String asString(@Nullable String def);

	@Contract("!null->!null")
	default @Nullable Number asNumber(@Nullable Number def) {
		@Nullable
		Number res = MathUtils.parseString(asString());
		return res == null ? def : res;
	}

	default @Nullable String asString() {
		return asString(null);
	}

	default @Nullable Number asNumber() {
		return asNumber(null);
	}

	@Contract(pure = true) default byte    asByte   (byte    def) { return asNumber(def        ).byteValue  (); }
	@Contract(pure = true) default short   asShort  (short   def) { return asNumber(def        ).shortValue (); }
	@Contract(pure = true) default int     asInt    (int     def) { return asNumber(def        ).intValue   (); }
	@Contract(pure = true) default long    asLong   (long    def) { return asNumber(def        ).longValue  (); }
	@Contract(pure = true) default float   asFloat  (float   def) { return asNumber(def        ).floatValue (); }
	@Contract(pure = true) default double  asDouble (double  def) { return asNumber(def        ).doubleValue(); }
	@Contract(pure = true) default boolean asBoolean(boolean def) { return asNumber(def ? 0 : 1).longValue() != 0; }

	@Contract(pure = true) default byte    asByte   () { return asByte   ((byte)0); }
	@Contract(pure = true) default short   asShort  () { return asShort  ((short)0); }
	@Contract(pure = true) default int     asInt    () { return asInt    (0); }
	@Contract(pure = true) default long    asLong   () { return asLong   (0); }
	@Contract(pure = true) default float   asFloat  () { return asFloat  (0); }
	@Contract(pure = true) default double  asDouble () { return asDouble (0); }
	@Contract(pure = true) default boolean asBoolean() { return asBoolean(false); }


	void remove();

	void setString(@Nullable String val);
	default void setNumber(@Nullable Number val) {
		if (val == null)
			remove();
		else
			setString("" + val);
	}

	default void setByte   (byte    val) { setNumber(val); }
	default void setShort  (short   val) { setNumber(val); }
	default void setInt    (int     val) { setNumber(val); }
	default void setLong   (long    val) { setNumber(val); }
	default void setFloat  (float   val) { setNumber(val); }
	default void setDouble (double  val) { setNumber(val); }
	default void setBoolean(boolean val) { setNumber(val ? 1 : 0); }
}
