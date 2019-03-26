package com.waho.util;

public class Protocol3762InfoDomain {

	private byte[] infoDomain;

	public Protocol3762InfoDomain() {
		super();
		this.infoDomain = new byte[6];
	}

	public byte[] nextInfoDomain() {
		this.infoDomain[5] += 0x01;
		if (this.infoDomain[5] == 0x00) {
			this.infoDomain[4] += 0x01;
			if (this.infoDomain[4] == 0x00) {
				this.infoDomain[3] += 0x01;
				if (this.infoDomain[3] == 0x00) {
					this.infoDomain[2] += 0x01;
					if (this.infoDomain[2] == 0x00) {
						this.infoDomain[1] += 0x01;
						if (this.infoDomain[1] == 0x00) {
							this.infoDomain[0] += 0x01;
						}
					}
				}
			}
		}
		byte[] result = new byte[6];
		System.arraycopy(this.infoDomain, 0, result, 0, 6);
		return result;
	}

}
