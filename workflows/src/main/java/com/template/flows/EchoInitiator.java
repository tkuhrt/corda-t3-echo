package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.IdentityService;
import net.corda.core.utilities.ProgressTracker;

import java.util.Set;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class EchoInitiator extends FlowLogic<Void> {
    private String message;
    private String counterparty;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public EchoInitiator(String message, String counterparty) {
        this.message = message;
        this.counterparty = counterparty;
    }
    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Initiator flow logic goes here.
        ServiceHub serviceHub = this.getServiceHub();
        IdentityService identityService = serviceHub.getIdentityService();
        Set<Party> parties = identityService.partiesFromName(this.counterparty, false);
        Party cp = parties.iterator().next();
        FlowSession session = initiateFlow(cp);
        System.out.print("Sending message: ");
        System.out.println(this.message);
        session.send(this.message);

        String response = session.receive(String.class).unwrap(s -> s);
        System.out.print("Received response: ");
        System.out.println(response);

        return null;
    }
}
