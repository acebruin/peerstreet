package peerstreet

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import com.opencsv.CSVReader

@Transactional
class PopulationService {

    private static final Long INVALID_CBSA = 99999
    private static final String MSA = 'Metropolitan Statistical Area'

    GrailsApplication grailsApplication

    private Map<Long, Long> zipToCbsaMap = new HashMap<Long, Long>()

    void loadZipToCbsa() {
        File file = grailsApplication.mainContext.getResource('zip_to_cbsa.csv').file

        CSVReader reader = new CSVReader(new FileReader(file), (char) ',')
        String[] nextLine = reader.readNext()

        while ((nextLine = reader.readNext()) != null) {
            Long cbsa = nextLine[1] as long
            if (cbsa != INVALID_CBSA) {
                zipToCbsaMap.put(nextLine[0] as long, nextLine[1] as long)
            }
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

    Map<String, String> fetchPopulationIndex(Long zip) {
        def result = [:]

        Long cbsa = zipToCbsaMap.get(zip)
        PopulationDb populationDb = PopulationDb.findByMdiv(cbsa)

        if (populationDb) {
            cbsa = populationDb.cbsa
        }

        populationDb = PopulationDb.findByCbsaAndLsad(cbsa, MSA)

        if (populationDb) {
            result.zip = zip
            result.cbsa = populationDb.cbsa
            result.name = populationDb.name
            result.pop2014 = populationDb.popEstimate2014
            result.pop2015 = populationDb.popEstimate2015
        }

        result
    }

}
