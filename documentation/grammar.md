# LL(1) Grammar

```
S  ::= E$

E  ::= T E'

E' ::= + T E' | - T E' |

T  ::= F T'

T' ::= * F T' | / F T' |

F  ::= id H | num | - F | ( E ) | O ( E )

H  ::= ^int |

O  ::= sin | cos | exp
```