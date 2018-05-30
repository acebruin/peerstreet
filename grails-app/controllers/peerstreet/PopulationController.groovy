package peerstreet

import org.springframework.beans.factory.annotation.Autowired

class PopulationController {

    @Autowired PopulationService populationService

    def index() {
        populationService.test()
        render 'Hello World'
    }

}
