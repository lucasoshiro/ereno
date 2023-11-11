package br.ufu.facom.ereno.evaluation;

import br.ufu.facom.ereno.evaluation.support.GeneralParameters;
import br.ufu.facom.ereno.evaluation.support.GenericEvaluation;
import br.ufu.facom.ereno.evaluation.support.Util;
import weka.core.Instances;

public class DatasetEval {
    public static void main(String[] args) throws Exception {
        runWithoutCV();
    }

    public static void runWithoutCV() throws Exception {
        GeneralParameters.DATASET = "E:\\ereno dataset\\hibrid_dataset_GOOSE_train.arff";
        Instances train = Util.loadSingleFile(false);
        train.setClassIndex(train.numAttributes() - 1);

//        System.out.println("Agora vem o segundo");
        GeneralParameters.DATASET = "E:\\ereno dataset\\hibrid_dataset_GOOSE_test.arff";
        Instances test = Util.loadSingleFile(false);
        test.setClassIndex(test.numAttributes() - 1);

        GenericEvaluation.runSingleClassifierJ48(train, train);
    }
}