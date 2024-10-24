package es.ucm.fdi.ici.c2324.practica3.grupo04.mspacman;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class IntervalDouble implements LocalSimilarityFunction{
	/** Interval */
	double _interval;

	/**
	 * Constructor.
	 */
	public IntervalDouble(double interval) {
		_interval = interval;
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 *            Double[]
	 * @param o2
	 *            Double[]
	 * @return result of apply the similarity function.
	 */
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof Double[]))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof Double[]))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		Double[] i1 = (Double[]) o1;
		Double[] i2 = (Double[]) o2;
		
		double totalSimilarity = 0.0;
		
		for(int i = 0; i < i1.length; i++) {
			if (i1[i] == null || i2[i] == null) {
	           
	            continue;
	        }
			double v1 = i1[i];
			double v2 = i2[i];
			totalSimilarity += 1 - ((double) Math.abs(v1 - v2) / _interval);
		}
		
		
		return totalSimilarity / i1.length;
	}
	
	/** Applicable to Integer */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof  Double[];
		else if(o2==null)
			return o1 instanceof  Double[];
		else
			return (o1 instanceof  Double[])&&(o2 instanceof  Double[]);
	}
}
