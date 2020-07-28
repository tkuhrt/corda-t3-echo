package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(EchoInitiator.class)
public class EchoResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public EchoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    private String reverseString(String input) {
        // getBytes() method to convert string
        // into bytes[].
        byte [] strAsByteArray = input.getBytes();

        byte [] result =
                new byte [strAsByteArray.length];

        // Store result in reverse order into the
        // result byte[]
        for (int i = 0; i<strAsByteArray.length; i++)
            result[i] =
                    strAsByteArray[strAsByteArray.length-i-1];

        return result.toString();
    }
    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Responder flow logic goes here.
        String message = this.counterpartySession.receive(String.class).unwrap(s -> s);
        System.out.print("Received message: ");
        System.out.println(message);
        String reversedMessage = this.reverseString(message);
        System.out.print("Responding with: ");
        System.out.println(reversedMessage);
        this.counterpartySession.send(reversedMessage);
        return null;
    }
}
