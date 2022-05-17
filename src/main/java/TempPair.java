public class TempPair<T> {
    private final T actualTemp;
    private final T feltTemp;

    public TempPair(T actualTemp, T feltTemp) {
        this.actualTemp = actualTemp;
        this.feltTemp = feltTemp;
    }

    public T getActualTemp() {
        return actualTemp;
    }

    public T getFeltTemp() {
        return feltTemp;
    }
}
