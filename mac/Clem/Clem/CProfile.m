//
//  CProfile.m
//  Clem
//
//  Created by Odric Roux-Paris on 10/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CProfile.h"
#import <AFNetworking/AFNetworking.h>

@implementation CProfile
@synthesize membreProfile;
@synthesize bloc;
@synthesize blocSave;
@synthesize blocProfilePK;

- (void)checkParametreProfil:(CCheckParamterProfilEnd)aBloc{
    bloc = [aBloc copy];
    
   
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[CToken defaultToken].accessToken]
                     forHTTPHeaderField:@"Authorization"];
    
    [manager GET:@"https://zestedesavoir.com/api/membres/mon_profil/" parameters:nil
         success:^(AFHTTPRequestOperation *operation, NSDictionary  *responseObject) {
             
             CMembre *m = [CMembre dictionnaryToMembre:responseObject];
             bloc(m, nil);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        bloc(nil, error);
        
    }];
    
   }

- (void)saveProfil:(CMembre *)saveMembre asBloc:(CSaveProfileBloc)aBloc{
    
    @throw ([NSException exceptionWithName:@"API de Zeste de Savoir en construction?" reason:@"En raison d'un bug, vous ne pouvez utilisez cette partie de l'api." userInfo:nil]);
    
    blocSave = [aBloc copy];
    
    NSDictionary *paramJSON = @{@"sign":@"My little Sign"};
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[CToken defaultToken].accessToken]
                     forHTTPHeaderField:@"Authorization"];
    
    
    [manager PUT:[NSString stringWithFormat:@"https://zestedesavoir.com/api/membres/%d/", saveMembre.pk]
      parameters:paramJSON
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
             
        
       } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
 
   
}
- (void)checkProfileWithPK:(int)pk bloc:(CCheckProfileWithPK)blocPK{
    blocProfilePK = [blocPK copy];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [manager GET:[NSString stringWithFormat:@"https://zestedesavoir.com/api/membres/%d/", pk]
      parameters:nil
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
             
        CMembre *m = [CMembre dictionnaryToMembre:responseObject];
        blocProfilePK(m, nil);
             
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        blocProfilePK(nil, error);
    }];
}
 
+ (instancetype)profile{
    return [[[self class] alloc] init];
}
@end
/*
@implementation CProfileAll

@synthesize blocProgress, blocReturnAllProfile, request, profile, totalMembres, currentMembres;

- (void)getAllProfile:(CDownloadEstimateProgress)pBloc
           allProfile:(CReturnAllProfile)AllBloc{
    
    blocProgress = [pBloc copy];
    blocReturnAllProfile = [AllBloc copy];
    
    //On fait la request
    [self getProfileWithSizePage:100 page:1];
    
    //On alloue le array pour les menbres;
    profile = [NSMutableArray array];
    
    
}

- (void)getProfileWithSizePage:(int)s page:(int)p{
    //On fait la request
    request = [CRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://zestedesavoir.com/api/membres/?page=%d&page_size=%d",p,s]] methode:@"GET" parameter:nil];
    [request sendRequestWithDelegate:self];
    

    
}

- (void)requestEnd:(NSString *)reponse{
    //On a terminé la request
    NSError *error;
    NSDictionary *reponseDico = [[CJSONDeserializer deserializer] deserializeAsDictionary:[reponse dataUsingEncoding:NSUTF8StringEncoding] error:&error];
    NSArray *resultM = reponseDico[@"results"];

    long totalM = [reponseDico[@"count"] longValue];
    long totalR = currentMembres + 100;
    //Instance de NSDATEFORMATTER
    NSDateFormatter *f = [[NSDateFormatter alloc] init];
    [f setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
    NSLog(@"%@",reponseDico[@"results"]);

    for (NSDictionary *dico in resultM){
        NSString *username = dico[@"username"];
        BOOL active = [dico[@"is_active"] boolValue];
        int pk = [dico[@"pk"] intValue];
        NSDate *join = [f dateFromString:dico[@"date_joined"]];
        
        CMembre *m = [[CMembre alloc] init];
        [m setUsername:username];
        [m setActive:active];
        [m setPk:pk];
        [m setDateJoined:join];
        
        [profile addObject:m];
    
    }
    
    
    blocProgress(totalM, totalR, error);
    
    
    currentMembres = totalR;
    
    //On fair une nouvelle request
    NSLog(@"%@",[reponseDico objectForKey:@"next"]);
    if ([reponseDico objectForKey:@"next"] != [NSNull null]){
       CRequest *r = [CRequest requestWithURL:[NSURL URLWithString:reponseDico[@"next"]] methode:@"GET" parameter:nil];
        
        [r sendRequestWithDelegate:self];
    }else{
        blocReturnAllProfile(profile, nil);
    }
    
    
}
- (void)requestError:(NSError *)error{
    
}



@end
 */
