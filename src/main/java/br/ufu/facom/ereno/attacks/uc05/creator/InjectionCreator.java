package br.ufu.facom.ereno.attacks.uc05.creator;

import br.ufu.facom.ereno.utils.GSVDatasetWritter;
import br.ufu.facom.ereno.benign.uc00.creator.MessageCreator;
import br.ufu.facom.ereno.benign.uc00.devices.IED;
import br.ufu.facom.ereno.messages.Goose;

import java.util.ArrayList;

import static br.ufu.facom.ereno.benign.uc00.devices.IED.randomBetween;

public class InjectionCreator implements MessageCreator {
    ArrayList<Goose> legitimateMessages;

    /**
     * @param legitimateMessages - previously generated legitimate messages
     */
    public InjectionCreator(ArrayList<Goose> legitimateMessages) {
        this.legitimateMessages = legitimateMessages;
    }

    @Override
    public void generate(IED ied, int numberofMessages) {
        for (int i = 0; i < numberofMessages; i++) {
            double minGoose = legitimateMessages.get(0).getTimestamp();
            double maxGoose = legitimateMessages.get(legitimateMessages.size() - 1).getTimestamp();

            if (minGoose >= maxGoose) {
                System.out.println("minGoose: " + minGoose);
                System.out.println("maxGoose: " + maxGoose);
                System.out.println(legitimateMessages.size()+" legitimate GOOSEs:");
                for (Goose g : legitimateMessages) {
                    System.out.println("GOOSE sq(" + g.getSqNum() + "/st" + g.getStNum() + ") em " + g.getTimestamp() + "(T: " + g.getT() + ")");
                }
                throw new IllegalArgumentException("Error on generating Injection messages (index: "+i+"/"+numberofMessages+") minGoose: " + minGoose  + " is higher than maxGoose: "+maxGoose);
            }

            double timestamp = randomBetween(minGoose, maxGoose);
            double t = randomBetween(minGoose, maxGoose);
            int stNum = randomBetween(0, 100);
            int sqNum = randomBetween(0, 100);
            int cbStatus = randomBetween(0, 2);
            int timeAllowedToLive = randomBetween(10000, 12000);
            int confRev = randomBetween(0, 100);

            // Last Goose Message from the random time
            Goose injectionMessage = new Goose(cbStatus, stNum, sqNum, timestamp, t, GSVDatasetWritter.label[5]);
            injectionMessage.setSqNum(sqNum);
            injectionMessage.setStNum(stNum);
            injectionMessage.setCbStatus(cbStatus);
            injectionMessage.setConfRev(confRev);
            injectionMessage.setGooseTimeAllowedtoLive(timeAllowedToLive);

            String[] legitimateMacAddress = {"FF:FF:FF:FF:FF:11", "FF:FF:FF:FF:FF:22", "FF:FF:FF:FF:FF:33", "FF:FF:FF:FF:FF:44", "FF:FF:FF:FF:FF:55", "FF:FF:FF:FF:FF:66", "FF:FF:FF:FF:FF:FF", "FF:FF:FF:FF:FF:77", "FF:FF:FF:FF:FF:AA", "FF:FF:FF:FF:FF:BB", "FF:FF:FF:FF:FF:CC", "FF:FF:FF:FF:FF:DD", "FF:FF:FF:FF:FF:EE", "FF:FF:FF:FF:FF:AB", "FF:FF:FF:FF:FF:AC"};
            Goose.ethDst = legitimateMacAddress[randomBetween(0, legitimateMacAddress.length - 1)];
            Goose.ethSrc = legitimateMacAddress[randomBetween(0, legitimateMacAddress.length - 1)];

            String[] ethTypes = {"0x000077b7", "0x000088b8", "0x88B8"};
            Goose.ethType = ethTypes[randomBetween(0, ethTypes.length - 1)];

            String[] appIDs = {"0x00003002", "0x00003001", "0x00003092", "0x00003011", "0x0043002", "0x00233001"};
            Goose.gooseAppid = appIDs[randomBetween(0, appIDs.length - 1)];

            String[] TPIDs = {"0x7101", "0x8100", "0x88B8"};
            Goose.TPID = TPIDs[randomBetween(0, TPIDs.length - 1)];

            String[] cobRefs = {"LD/LLN0$IntLockB", "LD/LLN0$GO$gcbA", "LD/LLN0$GO$gcblA"};
            Goose.gocbRef = cobRefs[randomBetween(0, TPIDs.length - 1)];

            String[] datasets = {"LD/LLN0$IntLockA", "AA1C1Q01A1LD0/LLN0$InterlockingC", "LD/LLN0$GO$gcblA"};
            Goose.datSet = datasets[randomBetween(0, datasets.length - 1)];

            String[] goIDs = {"InterlockingF", "InterlockingA", "IntLockA"};
            Goose.goID = goIDs[randomBetween(0, goIDs.length - 1)];

            String[] tests = {"TRUE", "FALSE"};
            Goose.test = tests[randomBetween(0, tests.length - 1)];

            String[] ndsCom = {"TRUE", "FALSE"};
            Goose.ndsCom = ndsCom[randomBetween(0, ndsCom.length - 1)];

            // Send the generated message to InjectorIED
            ied.addMessage(injectionMessage);

        }

    }
}
