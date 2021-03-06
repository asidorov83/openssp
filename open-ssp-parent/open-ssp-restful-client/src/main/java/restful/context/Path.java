package restful.context;

/**
 * @author André Schmer
 * 
 */
public enum Path {

	/**
	 * ssp-data-provider
	 */
	CORE("ssp-data-provider/lookup"),

	/**
	 * supplier
	 */
	SUPPLIER("supplier"),

	/**
	 * sspAdapter
	 */
	SSP_ADAPTER("sspAdapter"),

	/**
	 * eurref
	 */
	EUR_REF("eurref"),

	/**
	 * ?website=1
	 */
	WEBSITE("?website=1");

	private String value;

	private Path(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
