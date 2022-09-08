package br.ufu.facom.ereno.attacks.uc04.devices;

import br.ufu.facom.ereno.attacks.uc04.creator.MaqueradeFakeNormalCreator;
import br.ufu.facom.ereno.benign.uc00.devices.IED;
import br.ufu.facom.ereno.benign.uc00.devices.ProtectionIED;
import br.ufu.facom.ereno.messages.EthernetFrame;
import br.ufu.facom.ereno.messages.Goose;

import java.util.ArrayList;
import java.util.logging.Logger;

public class FakeNormalMasqueratorIED extends IED { // Masquerade attacks assume the attacker have full knowledge about the victim ProtectionIED

    protected ArrayList<Goose> masqueradeMessages; // The generated masquerade messages will be stored here

    ProtectionIED legitimateIED;

    public FakeNormalMasqueratorIED(ProtectionIED legitimate) {
        this.legitimateIED = legitimate;
        masqueradeMessages = new ArrayList<>();
    }

    @Override
    public void run(int numMasqueradeInstances) {
        Logger.getLogger("MasqueratorIED").info(
                "Feeding Masquerator IED with " + legitimateIED.getMessages().size() + " legitimate messages to generate " + numMasqueradeInstances + " masquerade fake normal.");
        messageCreator = new MaqueradeFakeNormalCreator(legitimateIED.getMessages()); // feeds the message creator with legitimate messages
        messageCreator.generate(this, numMasqueradeInstances); // pass itself to receive messages from generator
    }

    @Override
    public void addMessage(EthernetFrame message) {
        masqueradeMessages.add((Goose) message);
    }

    public ArrayList<Goose> getMasqueradeMessages() {
        return this.masqueradeMessages;
    }

    public int getNumberOfMessages() {
        return masqueradeMessages.size();
    }

    public ProtectionIED getLegitimateIED() {
        return this.legitimateIED;
    }
}
