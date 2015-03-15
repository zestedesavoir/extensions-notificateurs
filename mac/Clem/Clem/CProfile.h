//
//  CProfile.h
//  Clem
//
//  Created by Odric Roux-Paris on 10/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CMembre.h"

#import "CConnectLogin.h"
//#import "CConnectLogin.h"


typedef void(^CReturnAllProfile)(NSMutableArray *allProfile, NSError *error);
typedef void(^CDownloadEstimateProgress)(long totalMembre, long currentMembre, NSError *error);
typedef void(^CCheckParamterProfilEnd)(CMembre *membre, NSError *error);
typedef void(^CSaveProfileBloc)(BOOL success, NSError *error);


@interface CProfile : NSObject

@property (strong) CMembre *membreProfile;
@property (copy, nonatomic) CCheckParamterProfilEnd bloc;
@property (copy, nonatomic) CSaveProfileBloc blocSave;



- (void)checkParametreProfil:(CCheckParamterProfilEnd)aBloc;
- (void)saveProfil:(CMembre *)saveMembre asBloc:(CSaveProfileBloc)aBloc;



+ (instancetype)profile;
@end
/*
@interface CProfileAll : NSObject<CRequestDelegate>

@property (nonatomic, copy) CDownloadEstimateProgress blocProgress;
@property (nonatomic, copy) CReturnAllProfile blocReturnAllProfile;
@property long totalMembres;
@property long currentMembres;


@property CRequest *request;
@property NSMutableArray *profile;

- (void)getAllProfile:(CDownloadEstimateProgress)pBloc allProfile:(CReturnAllProfile)AllBloc;
- (void)getProfileWithSizePage:(int)s page:(int)p;

@end
 */
