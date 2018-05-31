package peerstreet

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.http.HttpServletResponse

class PopulationController {

    @Autowired PopulationService populationService

    def index() {
        if (!params.zip) {
            render status: HttpServletResponse.SC_BAD_REQUEST, text: "Zip code is needed."
            return
        }

        def popIndex = populationService.fetchPopulationIndex(params.zip as long)

        if (popIndex) {
            render popIndex as JSON
        }
        else {
            render status: 404, text: "Population record not found for zip: ${params.zip}"
        }
    }

}
