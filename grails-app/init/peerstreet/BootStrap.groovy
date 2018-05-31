package peerstreet

class BootStrap {

    PopulationService PopulationService

    def init = { servletContext ->
        populationService.loadZipToCbsa()
        populationService.loadCbsaToMsa()
    }

    def destroy = {
    }

}
