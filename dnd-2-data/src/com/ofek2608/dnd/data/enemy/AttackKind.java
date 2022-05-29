package com.ofek2608.dnd.data.enemy;

public enum AttackKind {
	HEAD { public float getProtection(float h, float c, float l, float b) { return h; } },
	BODY { public float getProtection(float h, float c, float l, float b) { return c; } },
	LEGS { public float getProtection(float h, float c, float l, float b) { return l; } },
	FEET { public float getProtection(float h, float c, float l, float b) { return b; } },
	GENERAL {
		public float getProtection(float h, float c, float l, float b) {
			return (h + c + l + b) * 0.25f;
		}
	},
	INNER {
		public float getProtection(float h, float c, float l, float b) {
			return 0;
		}
	};

	/**
	 * @param h helmet
	 * @param c chestplate
	 * @param l leggings
	 * @param b boots
	 * @return the protection value, between 0 and 1
	 */
	public abstract float getProtection(float h, float c, float l, float b);
}
