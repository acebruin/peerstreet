package peerstreet

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired

class PopulationController {

    @Autowired PopulationService populationService

    def index() {
        def popIndex = populationService.fetchPopulationIndex(params.zip as long)

        if (popIndex) {
            render popIndex as JSON
        }
        else {
            render status: 404, text: "Population record not found for zip: ${params.zip}"
        }
    }

}
