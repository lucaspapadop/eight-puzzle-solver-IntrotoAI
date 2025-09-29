# 8-Puzzle Solver: Search Algorithm Comparison

Comparative implementation of uninformed and informed search strategies 
for solving the 8-puzzle problem.

## Algorithms Implemented

1. **Uniform Cost Search** - Breadth-first expansion with visited state tracking
2. **A* (Manhattan Distance)** - h(n) = sum of tile distances from goal positions
3. **A* (Nilsson's Sequence)** - h(n) = 3×s(n) + p(n), where s(n) counts tiles 
   not followed by correct successor

## Performance Results

| Algorithm | Avg Nodes | Avg Time | Best Case | Worst Case |
|-----------|-----------|----------|-----------|------------|
| UCS       | 102,484   | 299ms    | 8,225     | 316,794    |
| A* Manhattan | 400    | 13ms     | 10        | 1,102      |
| A* Nilsson | 87       | 9ms      | 27        | 229        |

## Implementation Highlights

- **Custom data structures**: Built from scratch without Java Collections
- **Closed-list optimization**: HashSet-based duplicate detection
- **Heuristic computation**: Calculated in TreeNode constructors
- **Solution reconstruction**: Parent-pointer backtracking via stack

## Key Design Decisions

**Manhattan Distance Edge Cases**: Implemented corrections for diagonal 
wrapping issues (indices 2↔6, 3↔5) where naive calculation produces 
incorrect distances.

**Nilsson's Sequence**: Constructed "windmill" (clockwise perimeter) 
representation using HashMap for O(1) neighbor lookups.
