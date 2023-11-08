package br.ufu.facom.ereno.benign.uc00.creator;

import br.ufu.facom.ereno.benign.uc00.devices.IED;
import br.ufu.facom.ereno.benign.uc00.devices.MergingUnit;
import br.ufu.facom.ereno.messages.Sv;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SVCreator implements MessageCreator {
    private final String payloadFile;
    private MergingUnit mu; // This is the samambaia (sb) substation MU
    private ArrayList<Float[]> allElectricalMeassures;
    String columnsTitle[] = {
            "Time",
            "isbA", "isbB", "isbC",  // Current substation Samambaia
            "ismA", "ismB", "ismC",  // Current substation Serra da mesa
            "vsbA", "vsbB", "vsbC",  // Voltage Samambaia
            "vsmA", "vsmB", "vsmC"}; // Voltage substation Serra da mesa

    public SVCreator(String payloadFile) {
        this.payloadFile = payloadFile;
    }

    @Override
    public void generate(IED ied, int numberOfSVMessages) {
        this.mu = (MergingUnit) ied;
        this.allElectricalMeassures = consumeFloat(payloadFile, 1, columnsTitle);
        int messageCount = 0;
        for (Float[] column : allElectricalMeassures) {
            if (messageCount < numberOfSVMessages) {
                mu.addMessage(new Sv(column[0], column[1], column[2], column[3], column[7], column[8], column[9]));
            } else {
                break;
            }
        }
    }

    public ArrayList<Float[]> consumeFloat(String file, int scale, String columns[]) {
        ArrayList<Float[]> formatedCSVFile = new ArrayList<>();
        try {
            File myObj = new File(file);
            try (Scanner myReader = new Scanner(myObj)) {
                myReader.nextLine(); // Skip blank line
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    if (data.length() > 1) {
                        StringTokenizer stringTokenizer = new StringTokenizer(data, ",", true);
                        int tokenCount = 0;
                        Float[] tokenLine = new Float[columns.length];
                        while (stringTokenizer.hasMoreTokens()) {
                            tokenCount++;
                            String next = stringTokenizer.nextToken();
                            if (!next.contains(",")) {
                                if (!next.equals("normal") && !next.equals("fault")) {
                                    float token = Float.valueOf(next) * scale;
                                    int column = ((tokenCount + 1) / 2) - 1;
                                    tokenLine[column] = token;
                                }
                            }
                        }
                        formatedCSVFile.add(tokenLine);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erro: " + e.getLocalizedMessage());
        }
        return formatedCSVFile;
    }

}
