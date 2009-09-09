package jopt.csp.variable;

import java.util.Set;

/**
 * Interface for a class that is used to create constraints on set variables
 */
public interface CspSetConstraints {
    /**
     * Constraint representing intersection( X, Y ) = Z.
     * Restricts variable Z to be equal to the intersection of X and Y
     * 
     * @param x     First set variable used to create intersection
     * @param y     Second set variable used to create intersection
     * @param z     Set variable that is constrained to be equal to
     *                  intersection of X and Y
     * @return Intersection constraint that can be added to solver 
     */
    public CspConstraint eqIntersection(CspSetVariable x, CspSetVariable y, CspSetVariable z);
    
    /**
     * Constraint representing intersection( sources ) = target.
     * Restricts a target variable to be equal to the intersection of an 
     * array of source variables
     * 
     * @param sources   Array of variables used to create intersection
     * @param target    Set variable constrained equal to interesection of source variables
     * @return Intersection constraint that can be added to solver 
     */
    public CspConstraint eqIntersection(CspSetVariable sources[], CspSetVariable target);
    
    /**
     * Constraint representing partition( X, Y ) = Z.  This is similar to 
     * a union constraint except the source variables cannot share any common
     * values.
     * 
     * @param x         First variable used to form union constrained to have no common values with y
     * @param y         Second variable used to form union constrained to have no common values with x
     * @param z         Target variable that is constrained to be equal to the union of the sources
     * @return Partition constraint that can be added to solver 
     */
    public CspConstraint eqPartition(CspSetVariable x, CspSetVariable y, CspSetVariable z);
    
    /**
     * Constraint representing partition( sources ) = target.  This is similar to 
     * a union constraint except the source variables cannot share any common
     * values.
     * 
     * @param sources           Array of sources that form the union constrained to have no common values
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @return Partition constraint that can be added to solver 
     */
    public CspConstraint eqPartition(CspSetVariable sources[], CspSetVariable target);
    
    /**
     * Constraint representing partition( X, Y ) = Z that has more advanced filtering
     * than the {@link #eqPartition(CspSetVariable, CspSetVariable, CspSetVariable) normal eqPartition constraint}
     * Advanced filtering will reduce domains more than normal but takes longer to run.
     * 
     * @param x                 First variable used to form union constrained to have no common values with y
     * @param y                 Second variable used to form union constrained to have no common values with x
     * @param z                 Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed
     * @return Partition constraint that can be added to solver 
     */
    public CspConstraint eqPartition(CspSetVariable x, CspSetVariable y, CspSetVariable z, boolean advancedFilter);
    
    /**
     * Constraint representing partition( X, Y ) = Z that has more advanced filtering
     * than the {@link #eqPartition(CspSetVariable[], CspSetVariable) normal eqPartition constraint}. 
     * Advanced filtering will reduce domains more than normal but takes longer to run.
     * 
     * @param sources           Array of sources that form the union constrained to have no common values
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed
     * @return Partition constraint that can be added to solver 
     */
    public CspConstraint eqPartition(CspSetVariable sources[], CspSetVariable target, boolean advancedFilter);
    
    /**
     * Creates new union constraint representing union( X, Y ) = Z
     * 
     * @param x         First variable used to form union
     * @param y         Second variable used to form union
     * @param z         Target variable that is constrained to be equal to the union of the sources
     * @return Union constraint that can be added to solver 
     */
    public CspConstraint eqUnion(CspSetVariable x, CspSetVariable y, CspSetVariable z);

    /**
     * Creates new union constraint representing union( sources ) = target
     * 
     * @param sources           Array of sources that form the union
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @return Union constraint that can be added to solver 
     */
    public CspConstraint eqUnion(CspSetVariable sources[], CspSetVariable target);

    /**
     * Creates new union constraint that can utilize a variable that is the intersection
     * of X and Y variables to further reduce the target Z than the
     * {@link #eqUnion(CspSetVariable, CspSetVariable, CspSetVariable) normal eqUnion constraint}.
     * 
     * @param x         First variable used to form union
     * @param y         Second variable used to form union
     * @param z         Target variable that is constrained to be equal to the union of the sources
     * @param intersect This variable must be the intersection of X and Y for this constraint to
     *                      work properly
     * @return Union constraint that can be added to solver 
     */
    public CspConstraint eqUnion(CspSetVariable x, CspSetVariable y, CspSetVariable z, CspSetVariable intersect);
    
    /**
     * Creates new union constraint that will filter the domains more than the
     * {@link #eqUnion(CspSetVariable[], CspSetVariable) normal eqUnion constraint}, but will
     * take longer to run.
     *  
     * @param sources           Array of sources that form the union
     * @param target            Target variable that is constrained to be equal to the union of the sources
     * @param advancedFilter    True if advanced filtering should be performed
     * @return Union constraint that can be added to solver 
     */
    public CspConstraint eqUnion(CspSetVariable sources[], CspSetVariable target, boolean advancedFilter);

    /**
     * Creates new constraint representing null-intersection( sources )
     * 
     * @param sources   Array of variables that will share no common values
     * @return Intersection constraint that can be added to solver 
     */
    public CspConstraint nullIntersection(CspSetVariable sources[]);
    
    /**
     * Creates new constraint representing null-intersection( A, B )
     * 
     * @param a     First set variable in constraint
     * @param b     Second set variable in constraint
     * @return Constraint that will not allow A and B to have any common values
     */
    public CspConstraint nullIntersection(CspSetVariable a, CspSetVariable b);
    
    /**
     * Creates new constraint representing null-intersection( A, b )
     * 
     * @param a         Set variable to constrain
     * @param constb    Constant set of values not allowed in variable a
     * @return Constraint that will not allow A and b to have any common values
     */
    public CspConstraint nullIntersection(CspSetVariable a, Set constb);
    
    /**
     * Creates new constraint representing null-intersection( a, B )
     * 
     * @param consta    Constant set of values not allowed in variable B
     * @param b         Set variable to constrain
     * @return Constraint that will not allow a and B to have any common values
     */
    public CspConstraint nullIntersection(Set consta, CspSetVariable b);

    /**
     * Creates new constraint representing B is a subset of A
     * 
     * @param a     First set variable in constraint
     * @param b     Second set variable in constraint
     * @return Constraint forcing B to be a subset of A
     */
    public CspConstraint eqSubset(CspSetVariable a, CspSetVariable b);
    
    /**
     * Creates new constraint representing B is a subset of a
     * 
     * @param a     Constant set in constraint
     * @param b     Second set variable in constraint
     * @return Constraint forcing B to be a subset of a
     */
    public CspConstraint eqSubset(Set a, CspSetVariable b);
    
    /**
     * Creates new constraint representing b is a subset of A
     * 
     * @param a     First set variable in constraint
     * @param b     Constant set in constraint
     * @return Constraint forcing b to be a subset of A
     */
    public CspConstraint eqSubset(CspSetVariable a, Set b);

    /**
     * Creates new constraint representing B is a strict subset of A
     * 
     * @param a     First set variable in constraint
     * @param b     Second set variable in constraint
     * @return Constraint forcing B to be a strict subset of A
     */
    public CspConstraint strictSubset(CspSetVariable a, CspSetVariable b);
    
    /**
     * Creates new constraint representing B is a strict subset of a
     * 
     * @param a     Constant set in constraint
     * @param b     Second set variable in constraint
     * @return Constraint forcing B to be a strict subset of a
     */
    public CspConstraint strictSubset(Set a, CspSetVariable b);
    
    /**
     * Creates new constraint representing b is a strict subset of A
     * 
     * @param a     First set variable in constraint
     * @param b     Constant set in constraint
     * @return Constraint forcing b to be a strict subset of A
     */
    public CspConstraint strictSubset(CspSetVariable a, Set b);

    /**
     * Constrains a numeric expression to be a member of a set.  This function
     * does not support generic expressions.
     */
    public CspConstraint memberOfSet(CspIntSetVariable set, CspIntExpr expr);

    /**
     * Constrains a numeric expression to not be a member of a set.  This function
     * does not support generic expressions.
     */
    public CspConstraint notMemberOfSet(CspIntSetVariable set, CspIntExpr expr);
}

