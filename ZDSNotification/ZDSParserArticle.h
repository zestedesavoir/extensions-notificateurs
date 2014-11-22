//
//  ZDSParserArticle.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 18/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ZDSArticle.h"
@interface ZDSParserArticle : NSObject{


}

-(NSMutableArray *) parseArticle;
- (NSString *) parseString:(NSString *)string;


@end
