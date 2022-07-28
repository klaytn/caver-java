/*
 * Copyright 2020 The caver-java Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klaytn.caver.contract;

import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.request.KlayFilter;
import com.klaytn.caver.methods.response.LogsNotification;
import com.klaytn.caver.methods.response.Quantity;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.web3j.protocol.core.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * Representing a Contract's event information.
 */
public class ContractEvent {

    /**
     * The input type. It always set "event".
     */
    String type;

    /**
     * The event name.
     */
    String name;

    /**
     * The event signature string.
     */
    String signature;

    /**
     * The list of ContractIOType contains to event parameter information.
     */
    List<ContractIOType> inputs;

    /**
     * Creates a ContractEvent instance.
     */
    public ContractEvent() {
    }

    /**
     * Creates a ContractEvent instance.
     * @param type The input type. It always set "event"
     * @param name The event name
     * @param signature The event signature string.
     * @param inputs The list of ContractIOType contains to event parameter information.
     */
    public ContractEvent(String type, String name, String signature, List<ContractIOType> inputs) {
        this.type = type;
        this.name = name;
        this.signature = signature;
        this.inputs = inputs;
    }

    /**
     * Getter function for type.
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Getter function for name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Getter function for signature.
     * @return String
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Getter function for input.
     * @return List
     */
    public List<ContractIOType> getInputs() {
        return inputs;
    }

    /**
     * Setter function for name.
     * @param name A function name.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Setter function for type.
     * @param type The input type. It always set "event".
     */
    void setType(String type) {
        this.type = type;
    }

    /**
     * Setter function for inputs
     * @param inputs The list of ContractIOType contains to function parameter information.
     */
    void setInputs(List<ContractIOType> inputs) {
        this.inputs = inputs;
    }

    /**
     * Setter function for event signature.
     * @param signature A function signature
     */
    void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Returns a Flowable instance that can subscribe to a stream of notifications. <p>
     * Fills the KlayFilter object to be used when subscribing
     * using the event filter options received as parameters and the information of this event object. <p>
     *
     * <pre>Example :
     * {@code
     * ContractEvent event = contract.getEvents().get("Transfer");
     *
     * EventFilterOptions eventFilterOptions = new EventFilterOptions();
     * EventFilterOptions.IndexedParameter indexedParam = new EventFilterOptions.IndexedParameter("from", "0x{address in hex}");
     * eventFilterOptions.setFilterOptions(Arrays.asList(indexedParam));
     *
     * KlayFilter filter = new KlayFilter();
     * filter.setAddress(contract.getContractAddress());
     *
     * Flowable<LogsNotification> flowable = event.getFlowable(contract.getCaver(), eventFilterOptions, filter);
     * }
     * </pre>
     *
     * @param caver The name of the event in the contract.
     * @param paramsOption The filter events by indexed parameters.
     * @param filter TThe filter options to filter notification.
     * @return Flowable A Flowable instance that can subscribe to a stream of notifications
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Flowable<LogsNotification> getFlowable(Caver caver, EventFilterOptions paramsOption, KlayFilter filter) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // If the paramOption passed as a parameter by the user is not null,
        // the information in the paramOption is used to format to the KlayFilter topics
        // used in the Flowable object when actually subscribing to the event.
        if(paramsOption != null) {
            // If `filterOptions` field which has event type name and value is existed in the `EventFilterOptions`,
            // `filterOptions` will be encoded to the topics according to ABI spec.
            if(paramsOption.getFilterOptions() != null && !paramsOption.getFilterOptions().isEmpty()) {
                paramsOption.setTopicWithFilterOptions(this);
            }
            if(paramsOption.getTopics() != null && !paramsOption.getTopics().isEmpty()) {
                // If paramOptions has a topics field, set it directly in the KlayFilter's topics field.
                filter.setTopics(paramsOption.toKlayFilterTopic());
            }
        }

        final Flowable<LogsNotification> events = caver.rpc.klay.subscribeFlowable("logs", filter);
        return events;
    }
}
