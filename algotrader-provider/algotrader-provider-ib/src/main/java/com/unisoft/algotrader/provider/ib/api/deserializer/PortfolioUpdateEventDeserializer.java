package com.unisoft.algotrader.provider.ib.api.deserializer;

import com.unisoft.algotrader.model.refdata.Instrument;
import com.unisoft.algotrader.persistence.RefDataStore;
import com.unisoft.algotrader.provider.ib.IBProvider;
import com.unisoft.algotrader.provider.ib.InputStreamUtils;
import com.unisoft.algotrader.provider.ib.api.model.constants.IncomingMessageId;
import com.unisoft.algotrader.provider.ib.api.model.constants.OptionRight;
import com.unisoft.algotrader.provider.ib.api.model.constants.SecType;

import java.io.InputStream;

import static com.unisoft.algotrader.provider.ib.InputStreamUtils.*;

/**
 * Created by alex on 8/13/15.
 */
public class PortfolioUpdateEventDeserializer extends Deserializer {

    public PortfolioUpdateEventDeserializer(){
        super(IncomingMessageId.PORTFOLIO_UPDATE);
    }

    @Override
    public void consumeMessageContent(final int version, final InputStream inputStream, final IBProvider ibProvider) {
        final Instrument instrument = consumeInstrument(version, inputStream, ibProvider.getRefDataStore());
        final int position = readInt(inputStream);
        final double marketPrice = readDouble(inputStream);
        final double marketValue = readDouble(inputStream);
        double averageCost = 0;
        double unrealizedProfitAndLoss = 0;
        double realizedProfitAndLoss = 0;
        if (version >= 3) {
            averageCost = readDouble(inputStream);
            unrealizedProfitAndLoss = readDouble(inputStream);
            realizedProfitAndLoss = readDouble(inputStream);
        }
        String accountName = null;
        if (version >= 4) {
            accountName = readString(inputStream);
        }
        if ((version == 6) && (ibProvider.getIbSocket().getServerCurrentVersion() == 39)) {
            final String primaryExchange = readString(inputStream);
            //instrument.setExchId(exchId);
        }

        ibProvider.onPortfolioUpdateEvent(instrument, position, marketPrice, marketValue, averageCost, unrealizedProfitAndLoss,
                realizedProfitAndLoss, accountName);

    }


    protected Instrument consumeInstrument(final int version, final InputStream inputStream, final RefDataStore refDataStore) {
        final int instId = (version >= 6)? InputStreamUtils.readInt(inputStream) : 0;
        final String symbol = readString(inputStream);
        final Instrument.InstType instType = SecType.convert(readString(inputStream));
        final String expString = readString(inputStream);
        final double strike = InputStreamUtils.readDouble(inputStream);
        final Instrument.PutCall putCall = OptionRight.convert(readString(inputStream));
        final String multiplier = (version >= 7)?readString(inputStream) : null;
        final String primaryExchange = (version >= 7)?readString(inputStream) : null;
        final String ccyCode = readString(inputStream);
        final String localSymbol = (version >= 2)?readString(inputStream) : null;
        final String tradingClass = (version >= 8)? readString(inputStream): null;

        Instrument instrument = refDataStore.getInstrumentBySymbolAndExchange(IBProvider.PROVIDER_ID, symbol, primaryExchange);
        if (instrument == null){
            throw new RuntimeException("Cannot find instrumnet symbol=" + symbol +", primaryExchange="+primaryExchange);
        }

        return instrument;
    }


}