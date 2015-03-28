//
//  ICSearchMembre.m
//  Clem
//
//  Created by Odric Roux-Paris on 28/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CSearchMembre.h"
#import "CToken.h"
#import "CMembre.h"
#import <AFNetworking/AFNetworking.h>
@implementation CSearchMembre
@synthesize membre;
@synthesize searchBloc;
- (void)searchMembre:(NSString *)m bloc:(CSearchBloc)bloc{
    searchBloc = [bloc copy];
    membre = [NSMutableArray array];
    [self sendRequestWithURL:[NSString stringWithFormat:@"https://zestedesavoir.com/api/membres/?search=%@&page_size=100", m]];
}

- (void)sendRequestWithURL:(NSString *)url{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    if ([CToken defaultToken]){
        [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[CToken defaultToken].accessToken]
                         forHTTPHeaderField:@"Authorization"];
        
    }
    [manager GET:url
      parameters:nil
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
         
             NSDictionary *object = responseObject;
             

             
             NSDateFormatter *f = [[NSDateFormatter alloc] init];
             [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
             
             for (NSDictionary *dico in object[@"results"]){
                 NSString *username = dico[@"username"];
                 BOOL active = [dico[@"is_active"] boolValue];
                 int pk = [dico[@"pk"] intValue];
                 NSDate *join = [f dateFromString:dico[@"date_joined"]];
                 
                 CMembre *m = [[CMembre alloc] init];
                 [m setUsername:username];
                 [m setActive:active];
                 [m setPk:pk];
                 [m setDateJoined:join];
                 
                 [membre addObject:m];
                 
             }
             
             
             if (object[@"next"] != [NSNull null]){
                 [self sendRequestWithURL:object[@"next"]];
             }else{
                 searchBloc(membre, nil);
             }

             
         } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             searchBloc(nil, error);
         }];
    

    
}

+ (instancetype)searchMembre{
    return [[self alloc] init];
}

@end
