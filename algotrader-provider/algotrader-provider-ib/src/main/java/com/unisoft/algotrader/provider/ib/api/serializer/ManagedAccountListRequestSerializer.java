package com.unisoft.algotrader.provider.ib.api.serializer;

import com.unisoft.algotrader.provider.ib.api.model.system.OutgoingMessageId;

/**
 * Created by alex on 8/7/15.
 */
public class ManagedAccountListRequestSerializer extends Serializer{

    private static final int VERSION = 1;
    public ManagedAccountListRequestSerializer(int serverCurrentVersion){
        super(serverCurrentVersion);
    }

    public byte [] serialize(){
        ByteArrayBuilder builder = getByteArrayBuilder();
        builder.append(OutgoingMessageId.MANAGED_ACCOUNT_LIST_REQUEST.getId());
        builder.append(VERSION);
        return builder.toBytes();
    }
}
