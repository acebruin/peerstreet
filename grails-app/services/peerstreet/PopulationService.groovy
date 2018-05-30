package peerstreet

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import com.opencsv.CSVReader

@Transactional
class PopulationService {

    private static final Integer INVALID_CBSA = 99999;

    GrailsApplication grailsApplication

    private Map<Integer, Integer> zipToCbsaMap = new HashMap<Integer, Integer>()

    void loadZipToCbsa() {
        File file = grailsApplication.mainContext.getResource('zip_to_cbsa.csv').file

        CSVReader reader = new CSVReader(new FileReader(file), (char) ',')
        String[] nextLine = reader.readNext()

        while ((nextLine = reader.readNext()) != null) {
            zipToCbsaMap.put(nextLine[0] as int, nextLine[1] as int)
        }
    }

    void loadCbsaToMsa() {
        File file = grailsApplication.mainContext.getResource('cbsa_to_msa.csv').file

        CSVReader reader = new CSVReader(new FileReader(file), (char) ',')
        String[] nextLine = reader.readNext()
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[0]) {
                PopulationDb populationDb = new PopulationDb(
                        cbsa: nextLine[0] as long,
                        mdiv: nextLine[1] ? nextLine[1] as long : null,
                        name: nextLine[3],
                        lsad: nextLine[4],
                        popEstimate2014: nextLine[11] as long,
                        popEstimate2015: nextLine[12] as long,
                )

                if (populationDb.validate()) {
                    populationDb.save()
                }
            }
        }
    }

    void test() {
        println PopulationDb.count()
        println PopulationDb.findAllByCbsa(10180)
    }

}
