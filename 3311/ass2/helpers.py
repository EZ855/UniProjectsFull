# COMP3311 22T3 Assignment 2 ... Python helper functions
# add here any functions to share between Python scripts 
# you must submit this even if you add nothing

import re

# check whether a string looks like a year value
# return the integer value of the year if so

def getYear(year):
   digits = re.compile("^\d{4}$")
   if not digits.match(year):
      return None
   else:
      return int(year)

# prints principals in question 5
def printInfo(db, mid):
   query1 = '''
   select P.name, Pr.job, Pr.id
   from Movies M
      join Principals Pr on (M.id=Pr.movie)
      join People P on (Pr.person=P.id)
   where M.id = %s
   order by Pr.ord
   '''
   # and (Pr.job='actor' or Pr.job='actress' or Pr.job='self')
   
   query2 = '''
   select Psr.role
   from Playsrole Psr
      join Principals Pr on (Psr.inmovie=Pr.id)
   where Pr.id=%s
   '''
   
   db.execute(query1, [mid])

   principals = db.fetchall()
   for principal in principals:
      if principal[1] == 'actor' or principal[1] == 'actress' or principal[1] == 'self':
         db.execute(query2, [principal[2]])
         roleTup = db.fetchall()
         if not roleTup:
            print(principal[0] + " plays " + "???")
         else:
            print(principal[0] + " plays " + roleTup[0][0])
      else:
         print(principal[0] + ": " + principal[1])
   
