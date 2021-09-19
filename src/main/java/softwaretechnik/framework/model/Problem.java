package softwaretechnik.framework.model;

public interface Problem {
	public Solution createNewSolution() throws NoSolutionException;
}
