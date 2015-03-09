// CHECKSTYLE:OFF
/**
 * Source code generated by Fluent Builders Generator
 * Do not modify this file
 * See generator home page at: http://code.google.com/p/fluent-builders-generator-eclipse-plugin/
 */

package com.oforsky.mmm.ebo;

public class BidReqEboBuilder extends BidReqEboBuilderBase<BidReqEboBuilder> {
	public static BidReqEboBuilder bidReqEbo() {
		return new BidReqEboBuilder();
	}

	public BidReqEboBuilder() {
		super(new BidReqEbo());
	}

	public BidReqEbo build() {
		return getInstance();
	}
}

class BidReqEboBuilderBase<GeneratorT extends BidReqEboBuilderBase<GeneratorT>> {
	private BidReqEbo instance;

	protected BidReqEboBuilderBase(BidReqEbo aInstance) {
		instance = aInstance;
	}

	protected BidReqEbo getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withBidOid(Integer aValue) {
		instance.setBidOid(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withWarrantOid(Integer aValue) {
		instance.setWarrantOid(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withJobOid(Integer aValue) {
		instance.setJobOid(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withName(String aValue) {
		instance.setName(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withPrice(Double aValue) {
		instance.setPrice(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withApplyUnit(Integer aValue) {
		instance.setApplyUnit(aValue);

		return (GeneratorT) this;
	}

	@SuppressWarnings("unchecked")
	public GeneratorT withAmount(Integer aValue) {
		instance.setAmount(aValue);

		return (GeneratorT) this;
	}
}
