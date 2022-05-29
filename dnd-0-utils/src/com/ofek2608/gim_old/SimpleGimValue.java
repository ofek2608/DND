package com.ofek2608.gim_old;

import com.ofek2608.utils.MathUtils;

import javax.annotation.Nullable;

public class SimpleGimValue implements GimValue {
	private @Nullable String valueString;
	private @Nullable Number valueNumber;

	public SimpleGimValue() {}

	public SimpleGimValue(@Nullable Number val) {
		if (val != null) setNumber(val);
	}

	public SimpleGimValue(@Nullable String val) {
		if (val != null) setString(val);
	}

	@Override
	public boolean hasValue() {
		return valueString != null;
	}

	@Nullable
	@Override
	public String asString(@Nullable String def) {
		return valueString == null ? def : valueString;
	}

	@Nullable
	@Override
	public Number asNumber(@Nullable Number def) {
		return valueNumber == null ? def : valueNumber;
	}

	@Override
	public void remove() {
		valueString = null;
		valueNumber = null;
	}

	@Override
	public void setString(@Nullable String val) {
		if (val == null) {
			remove();
			return;
		}
		valueString = val;
		valueNumber = MathUtils.parseString(val);
	}

	@Override
	public void setNumber(@Nullable Number val) {
		if (val == null) {
			remove();
			return;
		}
		valueString = "" + val;
		valueNumber = val;
	}
}
