package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractAdDataBroker;
import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.core.broker.dto.BannerAdDto;
import com.atg.openssp.common.core.cache.type.BannerAdDataCache;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.context.Path;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * Act as broker between connector which loads the data from the webservice into a data transfer object and the cache.
 * 
 * This special data broker loads the {@see BannerAd} data from a backend which holds the data for the websites. It uses a {@see PathBuilder} object to store
 * information about the endpoint which is used by the {@see AbstractRemoteDataProvider} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class RemoteBanneradDataBroker extends AbstractAdDataBroker<BannerAdDto> {

	private static final Logger log = LoggerFactory.getLogger(RemoteBanneradDataBroker.class);

	public RemoteBanneradDataBroker() {}

	@Override
	protected PathBuilder getDefaulPathBuilder() {
		final PathBuilder pathBuilder = super.getDefaulPathBuilder();
		pathBuilder.addPath(Path.ADS_CORE);
		pathBuilder.addPath(Path.BANNER_ADS);
		return pathBuilder;
	}

	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final BannerAdDto dto = super.connect(BannerAdDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("BannerAdData", dto.getBannerAds().size(), startTS, endTS, endTS-startTS);
				log.debug("sizeof BannerAd data=" + dto.getBannerAds().size());
				for (BannerAd ad : dto.getBannerAds()) {
					BannerAdDataCache.instance.put(ad.getPlacementId(), ad);
				}
				return true;
			}
			log.error("no BannerAd data");
		} catch (final JsonSyntaxException | RestException | EmptyHostException e) {
            log.error(getClass() + ", " + e.getMessage(), e);
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() throws EmptyHostException {
		return getDefaulPathBuilder();
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		BannerAdDataCache.instance.switchCache();
	}

}
