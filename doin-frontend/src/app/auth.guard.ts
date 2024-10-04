/**
 * This is an auto-generated class from angular. Created using the command "ng generate guard auth".
 */
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from "./services/auth.service";
import {inject} from "@angular/core";
import {catchError, map, of} from "rxjs";

/**
 * This is method determines weather the specific route can be activated/accessed by the user.
 * </BR>
 * <p>
 *   Help creating this function was from <a href = https://www.geeksforgeeks.org/how-to-use-authguard-for-angular-17-routes/> here</a>.
 * </p>
 *
 * @param route
 * @param state
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = sessionStorage.getItem('token');

  // Check if there is a token
  if (token) {
    // Send token to the backend for validation
    return authService.validateToken(token).pipe(
      map(response => {
        if (response.valid) {
          // Token is valid, allow navigation
          return true;
        } else {
          // Token is invalid, redirect to login
          // router.navigate(['/login']);
          return false;
        }
      }),
      catchError(() => {
        // Handle error case, redirect to login
        // router.navigate(['/login']);
        return of(false); // Return false in case of error
      })
    );
  } else {
    // Token doesn't exist, redirect to login
    // router.navigate(['/login']);
    return of(false);
  }

};
