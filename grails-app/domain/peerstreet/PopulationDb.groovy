package peerstreet

class PopulationDb {

    Long id
    Long cbsa
    Long mdiv
    String name
    String lsad
    Long popEstimate2014
    Long popEstimate2015

    static constraints = {
        cbsa min:1L, nullable: false
        mdiv nullable: true
    }

    @Override
    public String toString() {
        return "PopulationDb{" +
                "cbsa=" + cbsa +
                ", mdiv=" + mdiv +
                ", name='" + name + '\'' +
                ", lsad='" + lsad + '\'' +
                ", popEstimate2014=" + popEstimate2014 +
                ", popEstimate2015=" + popEstimate2015 +
                '}';
    }

}
