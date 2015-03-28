//
//  CPRofileAll.m
//  Clem
//
//  Created by Odric Roux-Paris on 24/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CPRofileAll.h"
#import "CMembre.h"
#import "CToken.h"
#import <AFNetworking/AFNetworking.h>
@implementation CPRofileAll
@synthesize blocProgress, blocSuccess;
@synthesize currentMembre, totalMembre;
@synthesize membre;

- (id)init{
    self = [super init];
    if (self){
        membre = [NSMutableArray array];
    }
    return self;
}

- (void)getAllMembre:(CBlocProgress)progress sucess:(CBlocSuccess)success{
    blocProgress = [progress copy];
    blocSuccess = [success copy];
    
    [self sendRequestWithURL:@"https://zestedesavoir.com/api/membres/?page=1&page_size=100"];
    
    
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
             
             totalMembre = [object[@"count"] longValue];
             
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
                 currentMembre ++;
                 blocProgress(currentMembre, totalMembre);
                 
             }
             

             if (object[@"next"] != [NSNull null]){
                 [self sendRequestWithURL:object[@"next"]];
             }else{
                 blocSuccess(membre, nil);
             }

             


             
             
             
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            blocSuccess(nil, error);
         }];
    
}

+ (instancetype)profileAll{
    return [[self alloc] init];
}

@end
