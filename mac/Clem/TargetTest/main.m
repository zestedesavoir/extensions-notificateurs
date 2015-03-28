//
//  main.m
//  TargetTest
//
//  Created by Odric Roux-Paris on 14/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Clem/Clem.h>
int main(int argc, const char * argv[]) {
    @autoreleasepool {
        
        
        
        /*
        
        CConnectLogin *c = [[CConnectLogin alloc] initWithUsername:@"Cirdo"
                                                          password:@"ZQv-7AH-KQT-4cy"
                                                          clientID:@"A35VQbMSXQtLX4FJJK9vqHb5COYabF6ntpi.zt4h"
                                                      clientScrete:@"u:2@SgFgGvyqoEUJW-WylMUkhcHpQtfs_QNeExNTJ.RzknY6;Jis5Rc?2ob9N-HRZ:?X2lKxvF9Y1EmHlor8OwyWems;xdObycxSLJ5N;60nJM7jhX6C7v0!UMjfLF:x"];
         */
        
         /*
        
        
        
        CToken *token = [CToken tokenWithAcessToken:@"8nQ07495MPXK8eEhTQaMgZCODnfdu4" refreshToken:@"F9QUnYlilLLfyPRXvPQ1obAraW9EM3" expireIn:3600];
        [CToken setDefaultToken:token];
        CProfile *p = [CProfile profile];
         
        
        [p checkParametreProfil:^(CMembre *membre,
                                  NSError *error) {
            
            NSLog(@"%@", membre);
            membre.sign = @"Une petite signature.... :)";
            [p saveProfil:membre asBloc:^(BOOL success, NSError *error) {
                
            }];
        }];
          */

        
        
        /*
        [c connectionZDSWithBloc:^(CToken *t,
                                   NSError *error) {
            NSLog(@"%@", t.accessToken);
            CProfile *p = [CProfile profile];
           
            [p checkParametreProfil:^(CMembre *membre,
                                      NSError *error) {
                
                NSLog(@"%@", membre);
                membre.sign = @"Une petite signature.... :)";
                [p saveProfil:membre asBloc:^(BOOL success, NSError *error) {
                    
                }];
            }];
            
        }];
         */
       [[CSearchMembre searchMembre] searchMembre:@" " bloc:^(NSArray *membre, NSError *error) {
           
       }];
        
        [[NSRunLoop currentRunLoop] run];
        
       
        
        
        
        
    }
    return 0;
}
